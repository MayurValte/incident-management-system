pipeline {

    agent any

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    stages {

        stage('Debug') {
            steps {

                bat 'echo JAVA_HOME=%JAVA_HOME%'

                bat 'dir "%JAVA_HOME%"'

                bat 'java -version'

                bat 'where java'

                bat 'mvn -version'
            }
        }
    }
}
