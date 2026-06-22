pipeline {

    agent any

    stages {

        stage('Check Environment') {
            steps {
                bat 'java -version'
                bat 'where java'
                bat 'mvn -version'
                bat 'where mvn'
            }
        }

    }
}
