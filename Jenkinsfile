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

        stage('Prepare Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"

                        // Renomear imagens existentes como backup e emitir alerta
                        sh """
                            echo "Preparando Docker Hub: renomeando imagens existentes e removendo backups antigos..."

                            if docker pull ${DOCKER_REPO}:frontend-latest; then
                                docker tag ${DOCKER_REPO}:frontend-latest ${DOCKER_REPO}:frontend-backup1
                                docker rmi ${DOCKER_REPO}:frontend-latest
                                echo "Alerta: Imagem frontend renomeada para frontend-backup1 e frontend-latest removida."
                            else
                                echo "Nenhuma imagem frontend-latest encontrada para renomeação."
                            fi

                            if docker pull ${DOCKER_REPO}:backend-latest; then
                                docker tag ${DOCKER_REPO}:backend-latest ${DOCKER_REPO}:backend-backup1
                                docker rmi ${DOCKER_REPO}:backend-latest
                                echo "Alerta: Imagem backend renomeada para backend-backup1 e backend-latest removida."
                            else
                                echo "Nenhuma imagem backend-latest encontrada para renomeação."
                            fi

                            # Excluir backups antigos mantendo apenas o backup mais recente e emitir alerta
                            if docker images ${DOCKER_REPO} --format "{{.Repository}}:{{.Tag}}" | grep 'backup' | tail -n +2 | xargs -r docker rmi; then
                                echo "Alerta: Backups antigos removidos com sucesso."
                            else
                                echo "Nenhum backup adicional encontrado para remoção."
                            fi
                        """
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir(FRONTEND_DIR) {
                    script {
                        def frontendTag = "frontend-${new Date().format("yyyyMMdd-HHmmss")}"
                        sh "docker build -t ${DOCKER_REPO}:${frontendTag} -f Dockerfile ."
                        sh "docker tag ${DOCKER_REPO}:${frontendTag} ${DOCKER_REPO}:frontend-latest"
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir(BACKEND_DIR) {
                    script {
                        def backendTag = "backend-${new Date().format("yyyyMMdd-HHmmss")}"
                        sh "docker build -t ${DOCKER_REPO}:${backendTag} -f Dockerfile ."
                        sh "docker tag ${DOCKER_REPO}:${backendTag} ${DOCKER_REPO}:backend-latest"
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
