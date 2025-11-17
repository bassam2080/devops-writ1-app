pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    // Adding build steps (e.g. compilation, npm install, etc.)
                    echo 'Building the application...'
                }
            }
        }

        stage('Create Docker Image') {
            steps {
                script {
                    // Create Docker image
                    echo 'Creating Docker image...'
                    sh 'docker build -t myapp:${env.BUILD_ID} .'
                }
            }
        }

        stage('Push to Registry') {
            steps {
                script {
                    // Push the image to Docker registry
                    echo 'Pushing to registry...'
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin'
                        sh 'docker push myapp:${env.BUILD_ID}'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Deployment steps (e.g. Kubernetes, AWS, etc.)
                    echo 'Deploying the application...'
                }
            }
        }
    }
}