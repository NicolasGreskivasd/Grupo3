pipeline {
    agent any

    environment {
        DOCKER_REPO = 'nicolasgreskiv/pucpr-gh-pages' // Repositório Docker no Docker Hub
        KUBECONFIG_CRED = 'kubeconfig' // Nome da credencial kubeconfig para Kubernetes no Jenkins
        DOCKER_CRED = 'docker-hub-credentials' // Nome da credencial do Docker Hub no Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                // Faz o checkout do repositório
                checkout scm
            }
        }

        stage('Build Frontend') {
            steps {
                dir('projeto-web') {
                    script {
                        // Construindo a imagem do frontend
                        sh 'docker build -t $DOCKER_REPO/frontend:latest -f Dockerfile .'
                    }
                }
            }
        }

        stage('Build Backend JAR') {
            steps {
                dir('projeto-spring') {
                    script {
                        // Usando o Maven Wrapper para construir o JAR
                        sh './mvnw clean package'
                    }
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('projeto-spring') {
                    script {
                        // Construindo a imagem do backend
                        sh 'docker build -t $DOCKER_REPO/backend:latest -f Dockerfile .'
                    }
                }
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CRED) {
                        // Fazendo o push das imagens do Docker Hub
                        sh 'docker push $DOCKER_REPO/frontend:latest'
                        sh 'docker push $DOCKER_REPO/backend:latest'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Usando kubeconfig armazenado no Jenkins para acessar o cluster Kubernetes
                    withCredentials([file(credentialsId: KUBECONFIG_CRED, variable: 'KUBECONFIG')]) {
                        // Aplicando os manifests do Kubernetes para deployment e services
                        sh 'kubectl apply -f k8s/mysql-pv.yaml'
                        sh 'kubectl apply -f k8s/mysql-init-configmap.yaml'
                        sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                        sh 'kubectl apply -f k8s/mysql-service.yaml'
                        sh 'kubectl apply -f k8s/projeto-web.yaml'
                        sh 'kubectl apply -f k8s/projeto-spring.yaml'
                    }
                }
            }
        }
    }
}
