pipeline {
  agent any

  stages {
    // stage('Unit Test') {
    //   steps {
    //     script {
    //       sh './mvnw test'
    //     }
    //   }
    // }

    // stage ('AWS CodeGuru Security') {
    //   steps {
    //     script {
    //       sh """
    //         chmod +x run_codeguru_security.sh
    //         bash run_codeguru_security.sh spring-petclinic . us-east-1
    //       """
    //     }
    //   }
    // }

    // stage('SonarQube Analysis') {
    //   steps {
    //     withCredentials([string(credentialsId: 'sonar_token', variable: 'SONAR_TOKEN')]) {
    //       script {
    //         sh """
    //           export SONAR_TOKEN=${env.SONAR_TOKEN}
    //           ./mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=spring-petclinic-testing_petclinic
    //         """
    //       }
    //     } 
    //   }
    // }

    stage('Build') {
      steps {
        script {
          sh './mvnw package'
        }
      }
    }

    stage('Containerization') {
      steps {
        script {
          sh """
            docker build -t petclinic:${env.BUILD_NUMBER} .
            docker push jobin589/spring-petclinic:${env.BUILD_NUMBER}
          """
        }
      }
    }

    stage('Deploy') {
      steps {
        script {
          sh "kubectl run spring-petclinic --image=jobin589/spring-petclinic:${env.BUILD_NUMBER}"
        }
      }
    }

  }

  post {
    failure {
      script {
        def log = currentBuild.rawBuild.getLog(Integer.MAX_VALUE).join("\n")
        def failedStage = null

        echo log

        currentBuild.rawBuild.allActions.findAll { it instanceof org.jenkinsci.plugins.workflow.actions.FlowNodeAction }.each { action -> 
          action.failedNodeActions.each { flowNode -> 
            if (flowNode.error) { 
              failedStage = flowNode.displayName 
            } 
          } 
        }

        def stageLog = log.findAll { it.contains(failedStage) }.join("\n")
        writeFile file: 'error.log', text: stageLog

        withCredentials([usernamePassword(credentialsId: 'aws_creds', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
          sh """
            export AWS_ACCESS_KEY=${env.AWS_ACCESS_KEY_ID}
            export AWS_SECRET_KEY=${env.AWS_SECRET_ACCESS_KEY}

            python ai_error_analysis.py
            mkdir -p error_analysis
            rm -f error_analysis/* error.log
            mv ai_analysis.html error_analysis
          """
        }

        publishHTML (target : [allowMissing: false,
          alwaysLinkToLastBuild: true,
          keepAll: true,
          reportDir: 'error_analysis',
          reportFiles: 'ai_analysis.html',
          reportName: 'AI Error Analysis',
          reportTitles: 'AI Error Analysis'])
      }
    }
  }
}
