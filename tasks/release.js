const utils = require('./utils');
const path = require('path');
const childProcess = require('child_process');
const semver = require('semver');
const Promise = require('bluebird');
const _ = require('lodash');

const WORKING_DIR = path.resolve(__dirname, '../');

function exec(command, dir) {
  return utils.exec(command, dir || WORKING_DIR);
}

function updateMaster() {
  return Promise.coroutine(function* () {
    utils.log('Updating master branch...');
    yield exec('git checkout master');
    yield exec('git pull origin master');
  })();
}

function updateVersion(versionType, versionNumber) {
  return Promise.coroutine(function* () {
    utils.log('Updating version...');
    const currentVersion = yield utils.getProjectVersion();
    utils.log(`  Current version is ${currentVersion}`);
    const nextVersion = versionNumber || semver.inc(currentVersion, versionType);
    utils.log(`  Next version is ${nextVersion}`);
    yield utils.updateProjectVersion(nextVersion);
    return nextVersion;
  })();
}

function buildLibrary() {
  return Promise.coroutine(function* () {
    utils.log('Building library...');
    yield exec('npm run build');
  })();
}

function commitFiles(version) {
  return Promise.coroutine(function* () {
    utils.log('Committing files...');
    yield exec('git add --all');
    yield exec(`git commit -am "Release ${version}"`);
  })();
}

function pushFiles() {
  return Promise.coroutine(function* () {
    utils.log('Pushing files to remote...');
    yield exec('git push origin master');
  })();
}

function createTag(version) {
  return Promise.coroutine(function* () {
    utils.log(`Creating tag ${version}...`);
    yield exec(`git tag ${version}`);
  })();
}

function pushTag() {
  return Promise.coroutine(function* () {
    utils.log('Pushing tag to remote...');
    yield exec('git push --tags');
  })();
}

function validateArgs() {
  const versionType = process.argv[2];
  const versionNumber = process.argv[3];
  const versionTypes = ['major', 'minor', 'patch', 'version'];
  if (!_.includes(versionTypes, versionType) || (versionType === 'version' && !versionNumber)) {
    utils.log('Usage:');
    utils.log('npm run release -- major|minor|patch|version <version>');
    process.exit(1);
  }
  return {versionType, versionNumber};
}

// Run this if call directly from command line
if (require.main === module) {
  const {versionType, versionNumber} = validateArgs();
  Promise.coroutine(function* () {
    try {
      yield updateMaster();
      const version = yield updateVersion(versionType, versionNumber);
      utils.log(`Releasing version ${version} to remote...`);
      yield buildLibrary();
      yield commitFiles(version);
      yield pushFiles();
      yield createTag(version);
      yield pushTag();
      utils.log(`Version ${version} released with success!`);
    } catch (err) {
      utils.logError(err);
      process.exit(1);
    }
  })();
}
