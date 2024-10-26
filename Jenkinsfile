pipeline {
    agent any

    environment {
        DOCKER_REPO = 'nicolasgreskivasd/pucpr-gh-pages' // repositório Docker Hub em letras minúsculas
        KUBECONFIG_CRED = 'kubeconfig' // credencial kubeconfig para Kubernetes
        DOCKER_CRED = 'docker-hub-credentials' // credencial Docker Hub no Jenkins
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

        stage('Build Backend') {
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
                    // Login no Docker Hub e push das imagens
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CRED) {
                        sh 'docker push $DOCKER_REPO:frontend-latest'
                        sh 'docker push $DOCKER_REPO:backend-latest'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Usa o kubeconfig armazenado no Jenkins para acessar o cluster Kubernetes
                    withCredentials([file(credentialsId: KUBECONFIG_CRED, variable: 'KUBECONFIG')]) {
                        // Aplica os manifests do Kubernetes para deployment e services
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
