const fs = require('fs');
const path = require('path');
const util = require('util');
const childProcess = require('child_process');
const colors = require('colors/safe');
const Promise = require('bluebird');

const WORKING_DIR = path.resolve(__dirname, '../');
const GRADLE_FILE = path.join(WORKING_DIR, 'ayro/build.gradle');
const VERSION_NAME_REGEX = /versionName '(\d+\.\d+\.\d+)'/;
const VERSION_NAME_FORMAT = 'versionName \'%s\'';
const VERSION_CODE_REGEX = /versionCode (\d+)/;
const VERSION_CODE_FORMAT = 'versionCode %d';

const $ = this;

const readFileAsync = Promise.promisify(fs.readFile);
const writeFileAsync = Promise.promisify(fs.writeFile);

function logBuffer(data, buffer) {
  buffer += data.toString();
  const lines = buffer.split('\n');
  for (let i = 0; i < lines.length - 1; i += 1) {
    const line = lines[i];
    $.log(line, false);
  }
  return lines[lines.length - 1];
}


exports.getProjectVersion = () => {
  return Promise.coroutine(function* () {
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const match = VERSION_NAME_REGEX.exec(projectGradle);
    if (!match) {
      throw new Error('Could not find the project version name in gradle file');
    }
    return match[1];
  })();
};

exports.getProjectVersionCode = () => {
  return Promise.coroutine(function* () {
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const match = VERSION_CODE_REGEX.exec(projectGradle);
    if (!match) {
      throw new Error('Could not find the project version code in gradle file');
    }
    return match[1];
  })();
};

exports.updateProjectVersion = (version) => {
  return Promise.coroutine(function* () {
    const currentVersion = yield $.getProjectVersion();
    const currentVersionCode = Number(yield $.getProjectVersionCode());
    const nextVersionCode = currentVersionCode + 1;
    const projectGradle = yield readFileAsync(GRADLE_FILE, 'utf8');
    const updatedProjectGradle = projectGradle
      .replace(util.format(VERSION_NAME_FORMAT, currentVersion), util.format(VERSION_NAME_FORMAT, version))
      .replace(util.format(VERSION_CODE_FORMAT, currentVersionCode), util.format(VERSION_CODE_FORMAT, nextVersionCode));
    yield writeFileAsync(GRADLE_FILE, updatedProjectGradle);
  })();
};

exports.exec = (command, dir) => {
  return new Promise((resolve, reject) => {
    const child = childProcess.spawn(command, {
      shell: true,
      cwd: dir,
    });
    let outBuffer = '';
    let errBuffer = '';
    child.stdout.on('data', (data) => {
      outBuffer = logBuffer(data, outBuffer);
    });
    child.stderr.on('data', (data) => {
      errBuffer = logBuffer(data, errBuffer);
    });
    child.on('exit', (code) => {
      if (code === 0) {
        resolve();
      } else {
        reject(new Error(`Command "${command}" returned error`));
      }
    });
  });
};

exports.log = (text, colored) => {
  console.info(colored === false ? text : colors.green(text));
};

exports.logError = (text, colored) => {
  console.error(colored === false ? text : colors.red(text));
};
