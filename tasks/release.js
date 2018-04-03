const {releaseTask, commands} = require('@ayro/commons');
const fs = require('fs');
const path = require('path');
const util = require('util');
const Promise = require('bluebird');

const WORKING_DIR = path.resolve(__dirname, '../');
const GRADLE_FILE = path.join(WORKING_DIR, 'ayro/build.gradle');
const VERSION_NAME_REGEX = /versionName '(\d+\.\d+\.\d+)'/;
const VERSION_NAME_FORMAT = 'versionName \'%s\'';
const VERSION_CODE_REGEX = /versionCode (\d+)/;
const VERSION_CODE_FORMAT = 'versionCode %d';

const readFileAsync = Promise.promisify(fs.readFile);
const writeFileAsync = Promise.promisify(fs.writeFile);

function buildLibrary() {
  return Promise.coroutine(function* () {
    commands.log('Building library...');
    yield commands.exec('npm run build-prod', WORKING_DIR);
  })();
}

function getProjectVersion() {
  return Promise.coroutine(function* () {
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const match = VERSION_NAME_REGEX.exec(projectGradle);
    if (!match) {
      throw new Error('Could not find the project version name in gradle file');
    }
    return match[1];
  })();
};

function getProjectVersionCode() {
  return Promise.coroutine(function* () {
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const match = VERSION_CODE_REGEX.exec(projectGradle);
    if (!match) {
      throw new Error('Could not find the project version code in gradle file');
    }
    return match[1];
  })();
};

function updateProjectVersion(version) {
  return Promise.coroutine(function* () {
    const currentVersion = yield getProjectVersion();
    const currentVersionCode = Number(yield getProjectVersionCode());
    const nextVersionCode = currentVersionCode + 1;
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const updatedProjectGradle = projectGradle
      .replace(util.format(VERSION_NAME_FORMAT, currentVersion), util.format(VERSION_NAME_FORMAT, version))
      .replace(util.format(VERSION_CODE_FORMAT, currentVersionCode), util.format(VERSION_CODE_FORMAT, nextVersionCode));
    yield writeFileAsync(GRADLE_FILE, updatedProjectGradle);
  })();
};


// Run this if call directly from command line
if (require.main === module) {
  releaseTask.withWorkingDir(WORKING_DIR);
  releaseTask.withVersionUpdatedTask(updateProjectVersion);
  releaseTask.withBuildTask(buildLibrary);
  releaseTask.run(process.argv[2], process.argv[3]);
}