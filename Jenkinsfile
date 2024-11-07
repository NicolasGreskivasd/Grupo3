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

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'SonarQube Scanner'
                    
                    withCredentials([usernamePassword(credentialsId: 'sonarqube2', passwordVariable: 'SONAR_PASS', usernameVariable: 'SONAR_USER')]) {
                        withSonarQubeEnv('SonarQube Scan') {  // Use o nome configurado para o SonarQube no Jenkins
                            sh """
                                ${scannerHome}/bin/sonar-scanner \
                                -Dsonar.projectKey=Grupo3 \
                                -Dsonar.sources=. \
                                -Dsonar.login=$SONAR_USER \
                                -Dsonar.password=$SONAR_PASS
                            """
                        }
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir(FRONTEND_DIR) {
                    script {
                        sh "docker build --no-cache -t ${DOCKER_REPO}:frontend-latest -f Dockerfile ."
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir(BACKEND_DIR) {
                    script {
                        sh "docker build --no-cache -t ${DOCKER_REPO}:backend-latest -f Dockerfile ."
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
