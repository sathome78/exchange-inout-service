pipeline {
  
  agent any
    stages {
    stage('Maven install & Docker Build') {
      agent any
      steps {
        sh 'mvn clean install'
        sh 'mkdir opt'
        sh 'mkdir properties'
        sh 'cp -r /opt/properties/ ./opt/properties'
        sh 'docker build --build-arg ENVIRONMENT -t exrates/exrates-inout-service:$ENVIRONMENT .'
      }
    } 
    stage('Docker push') {
      agent any
      steps {
        sh 'docker tag exrates/exrates-inout-service:$ENVIRONMENT 172.50.50.7:5000/inoutservice:$ENVIRONMENT'
        sh 'docker push 172.50.50.7:5000/inoutservice:$ENVIRONMENT'
      }
    } 
    stage('Deploy container') {
      steps {
        sh 'docker -H tcp://172.50.50.7:2376 service update --image 172.50.50.7:5000/inoutservice:$ENVIRONMENT $ENVIRONMENT-inout-service'
      }
    }
  }  
}
