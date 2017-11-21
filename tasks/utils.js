const fs = require('fs');
const path = require('path');
const util = require('util');
const Promise = require('bluebird');

const WORKING_DIR = path.resolve(__dirname, '../');
const GRADLE_FILE = path.join(WORKING_DIR, 'chatz/build.gradle');
const VERSION_NAME_REGEX = /versionName '(\d+\.\d+\.\d+)'/;
const VERSION_NAME_FORMAT = 'versionName \'%s\'';

const readFileAsync = Promise.promisify(fs.readFile);
const writeFileAsync = Promise.promisify(fs.writeFile);

exports.getProjectVersion = () => {
  return Promise.coroutine(function* () {
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const match = VERSION_NAME_REGEX.exec(projectGradle);
    if (!match) {
      throw new Error('Could not find the project version in gradle file');
    }
    return match[1];
  })();
};

exports.updateProjectVersion = (version) => {
  return Promise.coroutine(function* () {
    const currentVersion = yield getProjectVersion();
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const updatedProjectGradle = projectGradle.replace(util.format(VERSION_NAME_FORMAT, currentVersion), util.format(VERSION_NAME_FORMAT, version));
    yield writeFileAsync(GRADLE_FILE, updatedProjectGradle);
  })();
};
