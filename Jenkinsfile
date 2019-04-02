pipeline {
  
  agent any
    stages {
    stage('Maven install & Docker Build') {
      agent any
      steps {
        sh 'mvn clean install'
        sh 'docker build --build-arg ENVIRONMENT -t exrates/exrates-inout-service:$ENVIRONMENT .'
      }
    } 
    stage('Docker pull') {
      agent any
      steps {
        sh 'docker tag roadtomoon/exrates-inout-service:$ENVIRONMENT 172.50.50.7:5000/inout-service:$ENVIRONMENT'
        sh 'docker push 172.50.50.7:5000/inout-service:$ENVIRONMENT'
      }
    } 
    stage('Deploy container') {
      steps {
        sh 'docker -H tcp://172.50.50.7:2376 service update --image 172.50.50.7:5000/inout-service:$ENVIRONMENT $ENVIRONMENT-inout-server'
      }
    }
  }  
}
