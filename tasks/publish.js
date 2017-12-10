const {publishTask, commands} = require('@ayro/commons');
const path = require('path');
const Promise = require('bluebird');

const WORKING_DIR = path.resolve(__dirname, '../');

function buildLibrary() {
  return Promise.coroutine(function* () {
    commands.log('Building library...');
    yield commands.exec('npm run build-prod', WORKING_DIR);
  })();
}

function publishToMavenCentral() {
  return Promise.coroutine(function* () {
    commands.log('Publishing to Maven central...');
    yield commands.exec('./gradlew uploadArchives', WORKING_DIR);
  })();
}

// Run this if call directly from command line
if (require.main === module) {
  publishTask.withWorkingDir(WORKING_DIR);
  publishTask.withBuildTask(buildLibrary);
  publishTask.withPublishTask(publishToMavenCentral);
  publishTask.run();
}
