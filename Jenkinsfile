pipeline {
    agent any

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
                        docker.build("nicolasgreskiv/pucpr-gh-pages:frontend-latest", "-f Dockerfile .")
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('projeto-spring') {
                    script {
                        docker.build("nicolasgreskiv/pucpr-gh-pages:backend-latest", "-f Dockerfile .")
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:frontend-latest"
                        sh "docker push nicolasgreskiv/pucpr-gh-pages:backend-latest"
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
                        // Aplicar Persistent Volume e Persistent Volume Claim antes dos deployments
                        sh "microk8s kubectl apply -f k8s/database-pv.yaml"
                        sh "microk8s kubectl apply -f k8s/database-pvc.yaml"
                        
                        // Aplicar os deployments
                        sh "microk8s kubectl apply -f k8s/database-deployment.yaml"
                        sh "microk8s kubectl apply -f k8s/backend-deployment.yaml"
                        sh "microk8s kubectl apply -f k8s/frontend-deployment.yaml"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
    }
}
