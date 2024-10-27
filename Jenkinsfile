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

        stage('Prepare Docker Hub Backup') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        sh """
                            echo "Preparando backup das imagens Docker Hub..."

                            docker pull ${DOCKER_REPO}:frontend-latest || true
                            docker tag ${DOCKER_REPO}:frontend-latest ${DOCKER_REPO}:frontend-backup || true

                            docker pull ${DOCKER_REPO}:backend-latest || true
                            docker tag ${DOCKER_REPO}:backend-latest ${DOCKER_REPO}:backend-backup || true

                            docker images ${DOCKER_REPO} --format '{{.Repository}}:{{.Tag}}' | grep 'backup' | tail -n +2 | xargs -r docker rmi || true
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
                        sh "docker build --no-cache -t ${DOCKER_REPO}:${frontendTag} -f Dockerfile ."
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
                        sh "docker build --no-cache -t ${DOCKER_REPO}:${backendTag} -f Dockerfile ."
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

        stage('Update YAML with Latest Image Tag') {
            steps {
                script {
                    // Atualizar os arquivos YAML com a tag gerada
                    sh """
                        sed -i 's|image: ${DOCKER_REPO}:frontend-latest|image: ${DOCKER_REPO}:${frontendTag}|' ${K8S_DIR}/frontend-deployment.yaml
                        sed -i 's|image: ${DOCKER_REPO}:backend-latest|image: ${DOCKER_REPO}:${backendTag}|' ${K8S_DIR}/backend-deployment.yaml
                    """
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

        stage('Force Update Deployments') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                        sh """
                            microk8s kubectl rollout restart deployment/backend-deployment
                            microk8s kubectl rollout restart deployment/frontend-deployment
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
