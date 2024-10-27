pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Frontend') {
            steps {
                dir('projeto-web') {
                    script {
                        sh 'docker build -t nicolasgreskiv/pucpr-gh-pages:frontend-latest -f Dockerfile .'
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('projeto-spring') {
                    script {
                        sh 'docker build -t nicolasgreskiv/pucpr-gh-pages:backend-latest -f Dockerfile .'
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        sh 'docker push nicolasgreskiv/pucpr-gh-pages:frontend-latest'
                        sh 'docker push nicolasgreskiv/pucpr-gh-pages:backend-latest'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh 'kubectl apply -f k8s/deployment.yaml'  // Ajuste o caminho se necessário
                        sh 'kubectl apply -f k8s/service.yaml'     // Ajuste o caminho se necessário
                    }
                }
            }
        }
    }
}
