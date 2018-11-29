pipeline {
  
  agent any
  
  stages {
    stage('Maven Install') {
      agent {
        docker {
          image 'maven:3.5.4'
        }
      }
      steps {
        sh 'mvn clean install'
      }
    }
    stage('Docker Build') {
      agent any
      steps {
        sh 'docker build --build-arg ENVIRONMENT -t roadtomoon/exrates-inout-service:$ENVIRONMENT .'
      }
    } 
    stage('Docker pull') {
      agent any
      steps {
        sh 'docker tag roadtomoon/exrates-inout-service:$ENVIRONMENT localhost:5000/inout-service:$ENVIRONMENT'
        sh 'docker push localhost:5000/inout-service:$ENVIRONMENT'
      }
    } 
    stage('Deploy container') {
      steps {
        sh 'docker -H tcp://localhost:2375 service update --image localhost:5000/inout-service:$ENVIRONMENT $ENVIRONMENT-inout-server'
      }
    }
  }  
}
