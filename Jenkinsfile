pipeline {
  agent any

  environment {
    DOCKERHUB = credentials('DockerHub')
    IMAGE_NAME = 'devops-writ1-app'
  }

  stages {
    stage('Build Java Application') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Docker Login') {
      steps {
        sh 'echo "$DOCKERHUB_PSW" | docker login -u "$DOCKERHUB_USR" --password-stdin'
      }
    }

    stage('Build and Run Tests') {
          steps {
            sh 'docker build -t --name devops-assignment-app ${IMAGE_NAME} .'
            sh 'cd CollegeCarPark && mvn test'
          }
    }

    stage('Build and Push Docker Image') {
      steps {
        sh '''
          docker tag ${IMAGE_NAME}:${BUILD_TAG} ${IMAGE_NAME}:latest
          docker push ${IMAGE_NAME}:${BUILD_TAG}
          docker push ${IMAGE_NAME}:latest
        '''
      }
    }

    stage('Deploy') {
      steps {
        sh '''
          docker stop devops-writ1-app || true
          docker rm devops-writ1-app || true
          docker compose up -d
        '''
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
