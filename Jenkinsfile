pipeline {

    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Verify') {
            steps {
                bat 'mvn -version'
            }
        }
    }
}
