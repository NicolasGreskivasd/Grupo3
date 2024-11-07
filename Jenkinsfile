pipeline {
    agent any

    stages {
        stage('Test SonarQube Scanner') {
            steps {
                script {
                    // Execute o comando para verificar a versão do SonarQube Scanner
                    sh '/path/to/sonar-scanner/bin/sonar-scanner --version'
                }
            }
        }
    }
}
