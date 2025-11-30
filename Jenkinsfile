pipeline {
  agent any

  environment {
    DOCKERHUB = credentials('DockerHub')
    IMAGE_NAME = 'DevOps-WRIT1-app'
  }

  stages {
    stage('Build Java Application') {
      steps {
        sh '''
          cd CollegeCarPark
          mvn clean package
        '''
      }
    }

    stage('Docker Login') {
      steps {
        sh 'echo "$DOCKERHUB_PSW" | docker login -u "$DOCKERHUB_USR" --password-stdin'
      }
    }

    stage('Build and Push Docker Image') {
      steps {
        sh '''
          docker build -t ${IMAGE_NAME} .
          docker tag ${IMAGE_NAME}:${BUILD_TAG} ${IMAGE_NAME}:latest
          docker push ${IMAGE_NAME}:${BUILD_TAG}
          docker push ${IMAGE_NAME}:latest
        '''
      }
    }

    stage('Deploy') {
      steps {
        sh '''
          docker stop DevOps-WRIT1-app || true
          docker rm DevOps-WRIT1-app || true
          docker compose up -d
        '''
      }
    }

    stage('Run Tests') {
      steps {
        sh 'cd CollegeCarPark && mvn test'
      }
    }
  }

  post {
    always {
      sh 'docker compose down || true'
      sh 'docker logout'
    }
  }
}
