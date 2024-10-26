pipeline {
    agent any
    environment {
        DOCKER_REPO = 'nicolasgreskiv/pucpr-gh-pages' // Repositório Docker no Docker Hub
        KUBECONFIG_CRED = 'kubeconfig' // Nome da credencial kubeconfig para Kubernetes no Jenkins
        DOCKER_CRED = 'docker-hub-credentials' // Nome da credencial do Docker Hub no Jenkins
    }
    stages {
        stage('Build Frontend') {
            steps {
                script {
                    // Build da aplicação frontend
                    sh 'docker build -t ${DOCKER_REPO}/frontend:latest -f projeto-web/Dockerfile projeto-web'
                }
            }
        }
        stage('Build Backend') {
            steps {
                script {
                    // Build da imagem Docker para o backend
                    sh 'docker build -t ${DOCKER_REPO}/backend:latest -f projeto-spring/Dockerfile projeto-spring'
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    // Login e push para o Docker Hub
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CRED}", passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        sh 'docker push ${DOCKER_REPO}/frontend:latest'
                        sh 'docker push ${DOCKER_REPO}/backend:latest'
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: "${KUBECONFIG_CRED}", variable: 'KUBECONFIG')]) {
                        sh 'kubectl apply -f k8s/deployment.yaml'
                        sh 'kubectl apply -f k8s/service.yaml'
                    }
                }
            }
        }
    }
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
