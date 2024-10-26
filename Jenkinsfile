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
                    dir('projeto-web') {
                        sh 'docker build -t ${DOCKER_REPO}/frontend:latest .'
                    }
                }
            }
        }
        stage('Build Backend') {
            steps {
                script {
                    dir('projeto-spring') {
                        sh 'docker build -t ${DOCKER_REPO}/backend:latest .'
                    }
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    sh "echo ${DOCKER_CRED} | docker login -u 'your-docker-username' --password-stdin"
                    sh 'docker push ${DOCKER_REPO}/frontend:latest'
                    sh 'docker push ${DOCKER_REPO}/backend:latest'
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh 'kubectl apply -f k8s/deployment.yaml'
                    sh 'kubectl apply -f k8s/service.yaml'
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
