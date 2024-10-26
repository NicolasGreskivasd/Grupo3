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
                dir('projeto-web') {
                    script {
                        // Build da imagem Docker para o front-end
                        sh 'docker build -t $DOCKER_REPO:frontend-latest -f Dockerfile .'
                    }
                }
            }
        }

        stage('Build Backend JAR') {
            steps {
                dir('projeto-spring') {
                    script {
                        // Compilar o projeto Spring Boot para gerar o arquivo JAR
                        sh './mvnw clean package'
                    }
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('projeto-spring') {
                    script {
                        // Build da imagem Docker para o back-end
                        sh 'docker build -t $DOCKER_REPO:backend-latest -f Dockerfile .'
                    }
                }
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CRED) {
                        // Enviar as imagens para o Docker Hub
                        sh 'docker push $DOCKER_REPO:frontend-latest'
                        sh 'docker push $DOCKER_REPO:backend-latest'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: KUBECONFIG_CRED, variable: 'KUBECONFIG')]) {
                        // Aplicar os arquivos de configuração YAML no Kubernetes
                        sh 'kubectl apply -f k8s/mysql-pv.yaml'
                        sh 'kubectl apply -f k8s/mysql-deployment.yaml'
                        sh 'kubectl apply -f k8s/projeto-web.yaml'
                        sh 'kubectl apply -f k8s/projeto-spring.yaml'
                    }
                }
            }
        }
    }
}
