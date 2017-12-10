const utils = require('./utils');
const {releaseTask, commands} = require('@ayro/commons');
const path = require('path');
const semver = require('semver');
const Promise = require('bluebird');

const WORKING_DIR = path.resolve(__dirname, '../');

function updateVersion(versionType, versionNumber) {
  return Promise.coroutine(function* () {
    commands.log('Updating version...');
    const currentVersion = yield utils.getProjectVersion();
    commands.log(`  Current version is ${currentVersion}`);
    const nextVersion = versionNumber || semver.inc(currentVersion, versionType);
    commands.log(`  Next version is ${nextVersion}`);
    yield utils.updateProjectVersion(nextVersion);
    return nextVersion;
  })();
}

function buildLibrary() {
  return Promise.coroutine(function* () {
    commands.log('Building library...');
    yield commands.exec('npm run build', WORKING_DIR);
  })();
}

// Run this if call directly from command line
if (require.main === module) {
  releaseTask.withWorkingDir(WORKING_DIR);
  releaseTask.withUpdateVersionTask(updateVersion);
  releaseTask.withBuildTask(buildLibrary);
  releaseTask.run(process.argv[2], process.argv[3]);
}