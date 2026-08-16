node('linux') {
  stage ('Poll') {
    checkout([
      $class: 'GitSCM', branches: [[name: '*/main']], extensions: [],
      userRemoteConfigs: [[url: 'https://github.com/zopencommunity/pillowport.git']]])
  }
  stage('Build') {
    build job: 'Port-Pipeline', parameters: [
      string(name: 'PORT_GITHUB_REPO', value: 'https://github.com/zopencommunity/pillowport.git'),
      string(name: 'PORT_DESCRIPTION', value: 'Imaging library for Python'),
      string(name: 'BUILD_LINE', value: 'STABLE'),
      booleanParam(name: 'PUBLISH_PYTHON_WHEEL', value: true)
    ]
  }
}
