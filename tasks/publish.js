const {publishTask, commands} = require('@ayro/commons');
const path = require('path');

const WORKING_DIR = path.resolve(__dirname, '../');

async function buildLibrary() {
  commands.log('Building library...');
  await commands.exec('npm run build-prod', WORKING_DIR);
}

async function publishToMavenCentral() {
  commands.log('Publishing to Maven central...');
  await commands.exec('./gradlew uploadArchives', WORKING_DIR);
}

// Run this if call directly from command line
if (require.main === module) {
  publishTask.withWorkingDir(WORKING_DIR);
  publishTask.withBuildTask(buildLibrary);
  publishTask.withPublishTask(publishToMavenCentral);
  publishTask.run();
}
