pipeline {
  agent any

  stages {
    stage('Unit Test') {
      steps {
        script {
          sh './mvnw test'
        }
      }
    }

    stage ('AWS CodeGuru Security') {
      steps {
        script {
          sh """
            chmod +x run_codeguru_security.sh
            bash run_codeguru_security.sh spring-petclinic . us-east-1
          """
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withCredentials([string(credentialsId: 'sonar_token', variable: 'SONAR_TOKEN')]) {
          script {
            sh """
              export SONAR_TOKEN=${env.SONAR_TOKEN}
              ./mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=spring-petclinic-testing_petclinic
            """
          }
        } 
      }
    }
  }
}