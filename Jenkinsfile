pipeline {
    agent any

    environment {
        DOCKER_REPO = 'nicolasgreskiv/pucpr-gh-pages'
        TIMESTAMP = new Date().format("yyyyMMdd-HHmmss")
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Frontend') {
            steps {
                dir('projeto-web') {
                    script {
                        docker.build("${DOCKER_REPO}:frontend-${TIMESTAMP}", "-f Dockerfile .")
                        sh "docker tag ${DOCKER_REPO}:frontend-${TIMESTAMP} ${DOCKER_REPO}:frontend-latest"
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('projeto-spring') {
                    script {
                        docker.build("${DOCKER_REPO}:backend-${TIMESTAMP}", "-f Dockerfile .")
                        sh "docker tag ${DOCKER_REPO}:backend-${TIMESTAMP} ${DOCKER_REPO}:backend-latest"
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                        
                        sh "docker push ${DOCKER_REPO}:frontend-${TIMESTAMP}"
                        sh "docker push ${DOCKER_REPO}:frontend-latest"
                        sh "docker push ${DOCKER_REPO}:backend-${TIMESTAMP}"
                        sh "docker push ${DOCKER_REPO}:backend-latest"
                    }
                }
            }
        }

        stage('Cleanup Old Images on Docker Hub') {
            steps {
                script {
                    // Limpeza para manter as 3 últimas imagens frontend e backend
                    sh """
                    docker login -u \$DOCKER_USER -p \$DOCKER_PASS
                    for tag in \$(curl -s https://hub.docker.com/v2/repositories/${DOCKER_REPO}/tags/?page_size=100 | jq -r '.results | .[] | .name' | grep '^frontend-' | sort -r | tail -n +4); do
                        docker rmi ${DOCKER_REPO}:\$tag || true
                    done
                    for tag in \$(curl -s https://hub.docker.com/v2/repositories/${DOCKER_REPO}/tags/?page_size=100 | jq -r '.results | .[] | .name' | grep '^backend-' | sort -r | tail -n +4); do
                        docker rmi ${DOCKER_REPO}:\$tag || true
                    done
                    """
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
                        sh "microk8s kubectl apply -f k8s/create-base-configmap.yaml"
                        sh "microk8s kubectl apply -f k8s/database-pv.yaml"
                        sh "microk8s kubectl apply -f k8s/database-service.yaml"
                        sh "microk8s kubectl apply -f k8s/database-statefulset.yaml"
                        sh "microk8s kubectl apply -f k8s/backend-deployment.yaml"
                        sh "microk8s kubectl apply -f k8s/backend-service.yaml"
                        sh "microk8s kubectl apply -f k8s/frontend-deployment.yaml"
                        sh "microk8s kubectl apply -f k8s/frontend-service.yaml"
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
