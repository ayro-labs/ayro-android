const utils = require('./utils');
const path = require('path');
const childProcess = require('child_process');
const semver = require('semver');
const Promise = require('bluebird');

const WORKING_DIR = path.resolve(__dirname, '../');

const execAsync = Promise.promisify(childProcess.exec);

function exec(command, options) {
  return execAsync(command, options || {cwd: WORKING_DIR});
}

function updateMaster() {
  return Promise.coroutine(function* () {
    console.log('Updating master branch...');
    yield exec('git checkout master');
    yield exec('git pull origin master');
  })();
}

function updateVersion(versionType) {
  return Promise.coroutine(function* () {
    console.log('Updating version...');
    const version = yield utils.getProjectVersion();
    console.log(`  Current version is ${version}`);
    const nextVersion = semver.inc(version, versionType);
    console.log(`  Next version is ${nextVersion}`);
    yield utils.updateProjectVersion(nextVersion);
    return nextVersion;
  })();
}

function buildLibrary() {
  return Promise.coroutine(function* () {
    console.log('Building library...');
    yield exec('npm run build');
  })();
}

function commitFiles(version) {
  return Promise.coroutine(function* () {
    console.log('Committing files...');
    yield exec('git add --all');
    yield exec(`git commit -am "Release ${version}"`);
  })();
}

function pushFiles() {
  return Promise.coroutine(function* () {
    console.log('Pushing files to remote...');
    yield exec('git push origin master');
  })();
}

function createTag(version) {
  return Promise.coroutine(function* () {
    console.log(`Creating tag ${version}...`);
    yield exec(`git tag ${version}`);
  })();
}

function pushTag() {
  return Promise.coroutine(function* () {
    console.log('Pushing tag to remote...');
    yield exec('git push --tags');
  })();
}

// Run this if call directly from command line
if (require.main === module) {
  const versionType = process.argv[2];
  if (!versionType || ['major', 'minor', 'patch'].indexOf(versionType) === -1) {
    console.log('Usage:');
    console.log('npm run release -- major|minor|patch');
    process.exit(1);
  }
  Promise.coroutine(function* () {
    try {
      yield updateMaster();
      const version = yield updateVersion(versionType);
      console.log(`Releasing version ${version} to remote...`);
      yield buildLibrary();
      yield commitFiles(version);
      yield pushFiles();
      yield createTag(version);
      yield pushTag();
      console.log(`Version ${version} released with success!`);
    } catch (err) {
      console.error(err);
      process.exit(1);
    }
  })();
}
