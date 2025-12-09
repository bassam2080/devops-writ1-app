pipeline {
  agent any

  environment {
    DOCKERHUB = credentials('DockerHub')
    IMAGE_NAME = "${DOCKERHUB_USR}/devops-writ1-app"
    IMAGE_TAG = "${BUILD_NUMBER}"
  }

  stages {
    stage('Build Docker Image') {
      steps {
        sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest .'
      }
    }

    stage('Push to DockerHub') {
      steps {
        sh '''
          docker login -u "$DOCKERHUB_USR" -p "$DOCKERHUB_PSW"
          docker push ${IMAGE_NAME}:${IMAGE_TAG}
          docker push ${IMAGE_NAME}:latest
        '''
      }
    }

    stage('Deploy') {
      steps {
        sh '''
          docker compose down || true
          docker compose up -d
        '''
      }
    }
  }

  post {
    always {
      sh 'docker logout'
    }
  }
}