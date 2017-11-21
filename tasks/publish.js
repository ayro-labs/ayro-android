const utils = require('./utils');
const path = require('path');
const childProcess = require('child_process');
const propertiesParser = require('properties-parser');
const GitHubApi = require('github');
const Promise = require('bluebird');

const REPOSITORY_NAME = 'chatz-android';
const REPOSITORY_OWNER = 'chatz-io';
const WORKING_DIR = path.resolve(__dirname, '../');
const GRADLE_PROPERTIES = path.resolve(__dirname, '../gradle.properties');

const execAsync = Promise.promisify(childProcess.exec);

Promise.promisifyAll(propertiesParser);

const gitHubApi = new GitHubApi();
gitHubApi.authenticate({
  type: 'token',
  token: '49aa7a1e572c681a5f7773983359a57e71a15b30',
});

function exec(command, options) {
  return execAsync(command, options || {cwd: WORKING_DIR});
}

function getProjectVersion() {
  return utils.getProjectVersion();
}

function checkoutTag(version) {
  return Promise.coroutine(function* () {
    console.log(`Checking out the tag ${version}...`);
    yield exec(`git checkout ${version}`);
  })();
}

function buildLibrary() {
  return Promise.coroutine(function* () {
    console.log('Building library...');
    yield exec('npm run build-prod');
  })();
}

function editGradleProperties() {
  return Promise.coroutine(function* () {
    const properties = yield propertiesParser.createEditor(GRADLE_PROPERTIES);
    properties.set('VERSION_NAME', '1.0.0');
    properties.set('VERSION_CODE', '1');
    properties.set('POM_ARTIFACT_ID', 'chatz');
    properties.set('POM_NAME', 'Chatz');
    properties.set('POM_DESCRIPTION', 'Chatz Android SDK');
    yield propertiesParser.save();
  })();
}

function publishMavenCentral() {
  return Promise.coroutine(function* () {
    console.log('Publishing to Maven central...');
    // yield exec('./gradlew build clean uploadArchive');
  })();
}

// Run this if call directly from command line
if (require.main === module) {
  Promise.coroutine(function* () {
    try {
      const version = yield getProjectVersion();
      console.log(`Publishing version ${version} to Maven central...`);
      yield checkoutTag(version);
      yield buildLibrary();
      yield editGradleProperties();
      yield publishToMavenCentral();
      yield checkoutTag('master');
      console.log(`Version ${version} published with success!`);
    } catch (err) {
      console.error(err);
      process.exit(1);
    }
  })();
}
