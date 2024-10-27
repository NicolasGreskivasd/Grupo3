pipeline {
    agent any

    environment {
        DOCKER_REPO = 'nicolasgreskiv/pucpr-gh-pages'
        FRONTEND_DIR = 'projeto-web'
        BACKEND_DIR = 'projeto-spring'
        K8S_DIR = 'k8s'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Clear Local Docker Cache and Images') {
            steps {
                script {
                    // Limpar todas as imagens e cache local do Docker
                    sh """
                        echo "Removendo todas as imagens e cache do Docker local..."
                        docker system prune -af --volumes || true
                    """
                }
            }
        }

        stage('Clear Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"

                        // Remover imagens existentes do Docker Hub
                        sh """
                            echo "Removendo todas as imagens antigas no Docker Hub..."
                            docker rmi -f ${DOCKER_REPO}:frontend-latest || true
                            docker rmi -f ${DOCKER_REPO}:backend-latest || true
                        """
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir(FRONTEND_DIR) {
                    script {
                        def frontendTag = "frontend-latest"
                        sh "docker build --no-cache -t ${DOCKER_REPO}:${frontendTag} -f Dockerfile ."
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir(BACKEND_DIR) {
                    script {
                        def backendTag = "backend-latest"
                        sh "docker build --no-cache -t ${DOCKER_REPO}:${backendTag} -f Dockerfile ."
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "docker push ${DOCKER_REPO}:frontend-latest"
                        sh "docker push ${DOCKER_REPO}:backend-latest"
                    }
                }
            }
        }

        stage('Test kubectl') {
            steps {
                script {
                    sh 'microk8s kubectl version --client'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh """
                            microk8s kubectl apply -f ${K8S_DIR}/create-base-configmap.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/database-pv.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/database-service.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/database-statefulset.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/backend-deployment.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/backend-service.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/frontend-deployment.yaml
                            microk8s kubectl apply -f ${K8S_DIR}/frontend-service.yaml
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}
