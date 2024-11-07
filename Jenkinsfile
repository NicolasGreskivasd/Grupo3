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

        stage('Test SonarQube Scanner') {
            steps {
                script {
                    def scannerHome = tool 'SonarQubeScanner'
                    // Testa se o scanner está acessível
                    sh "${scannerHome}/bin/sonar-scanner --version"
                }
            }
        }

}
