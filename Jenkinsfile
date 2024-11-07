pipeline {
    agent any

    environment {
        DOCKER_REPO = 'nicolasgreskiv/pucpr-gh-pages'
        FRONTEND_DIR = 'projeto-web'
        BACKEND_DIR = 'projeto-spring'
        K8S_DIR = 'k8s'
        SONARQUBE_SERVER = 'SonarQubeScanner'
        SONARQUBE_PROJECT_KEY = 'Grupo3'
        SONARQUBE_LOGIN = 'sqa_d7016240742765f2a8ee37684c6bd531e9251519'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Java Project') {
            steps {
                dir(BACKEND_DIR) {
                    sh 'mvn clean compile' // Compila o projeto Java
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv("${SONARQUBE_SERVER}") {
                        sh """
                        /opt/sonar-scanner/bin/sonar-scanner \
                        -Dsonar.projectKey=${SONARQUBE_PROJECT_KEY} \
                        -Dsonar.sources=. \
                        -Dsonar.java.binaries=${BACKEND_DIR}/target/classes \
                        -Dsonar.login=${SONARQUBE_LOGIN}
                        """
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
