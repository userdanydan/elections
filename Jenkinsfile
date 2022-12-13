pipeline{
    agent any
    tools {
        gradle '7.6'
        jdk '17'
    }
    stages{
        stage('Buzz Build') {
            steps {
                sh '''
                    env | grep -e PATH -e JAVA_HOME
                    which java
                    java --version
                '''
                sh 'gradle build'
            }
        }
        stage('Bees') {
            steps {
                echo 'Bees buzzing'
            }
        }
    }
}