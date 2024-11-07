pipeline {
    agent any

    environment {
        DOCKER_REPO = 'nicolasgreskiv/pucpr-gh-pages'
        FRONTEND_DIR = 'projeto-web'
        BACKEND_DIR = 'projeto-spring'
        K8S_DIR = 'k8s'
        SONARQUBE_SERVER = 'SonarQube Server'  // Nome configurado no Jenkins para o servidor SonarQube
        SONARQUBE_PROJECT_KEY = 'Grupo3' // Chave do projeto no SonarQube
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
                    withSonarQubeEnv("${SONARQUBE_SERVER}") {
                        sh "sonar-scanner -Dsonar.projectKey=${SONARQUBE_PROJECT_KEY} -Dsonar.sources=. -Dsonar.login=${env.SONAR_AUTH_TOKEN}"
                    }
                }
            }
            post {
                success {
                    echo 'SonarQube analysis completed successfully.'
                }
                failure {
                    error 'SonarQube analysis failed, stopping the build.'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir(FRONTEND_DIR) {
                    sh "docker build --no-cache -t ${DOCKER_REPO}:frontend-latest -f Dockerfile ."
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir(BACKEND_DIR) {
                    sh "docker build --no-cache -t ${DOCKER_REPO}:backend-latest -f Dockerfile ."
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                    sh "docker push ${DOCKER_REPO}:frontend-latest"
                    sh "docker push ${DOCKER_REPO}:backend-latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
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

    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}
