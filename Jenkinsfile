def FAILED_STAGE

pipeline {
  agent any

  stages {
    // stage('Unit Test') {
    //   steps {
    //     script {
    //       FAILED_STAGE=env.STAGE_NAME
    //       sh './mvnw test'
    //     }
    //   }
    // }

    // stage ('AWS CodeGuru Security') {
    //   steps {
    //     script {
    //       FAILED_STAGE=env.STAGE_NAME
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
    //         FAILED_STAGE=env.STAGE_NAME
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
          FAILED_STAGE=env.STAGE_NAME
          sh './mvnw clean package'
        }
      }
    }

    stage('Containerization') {
      steps {
        script {
          FAILED_STAGE=env.STAGE_NAME
          sh """
            docker build -t jobin589/spring-petclinic:${env.BUILD_NUMBER} .
            docker push myimage
          """
        }
      }
    }

    // stage('Deploy') {
    //   steps {
    //     script {
    //       FAILED_STAGE=env.STAGE_NAME
    //       sh "kubectl run spring-petclinic --image=jobin589/spring-petclinic:${env.BUILD_NUMBER}"
    //     }
    //   }
    // }

  }

  post {
    failure {
      // script {
      //   def log = currentBuild.rawBuild.getLog(Integer.MAX_VALUE)
      //   def stageLog = []
      //   def stageFound = false

      //   log.each { line ->
      //     if (line.contains("[Pipeline] { (" + FAILED_STAGE + ")")) {
      //         stageFound = true
      //     }
      //     if (stageFound) {
      //         stageLog.add(line)
      //     }
      //     if (stageFound && line.contains("[Pipeline] }")) {
      //         stageFound = false
      //     }
      //   }
        
      //   def stageLogString = stageLog.join("\n")
      //   writeFile file: 'error.log', text: stageLogString
      //   def logString = log.join("\n")
      //   writeFile file: 'pipeline.log', text: logString

      //   // withCredentials([usernamePassword(credentialsId: 'aws_creds', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
      //   //   sh """
      //   //     export AWS_ACCESS_KEY=${env.AWS_ACCESS_KEY_ID}
      //   //     export AWS_SECRET_KEY=${env.AWS_SECRET_ACCESS_KEY}

      //   //     cat error.log
      //   //     python ai_error_analysis.py
      //   //     mkdir -p error_analysis
      //   //     rm -f error_analysis/* error.log
      //   //     mv ai_analysis.html error_analysis
      //   //   """
      //   // }

      //   // publishHTML (target : [allowMissing: false,
      //   //   alwaysLinkToLastBuild: true,
      //   //   keepAll: true,
      //   //   reportDir: 'error_analysis',
      //   //   reportFiles: 'ai_analysis.html',
      //   //   reportName: 'AI Error Analysis',
      //   //   reportTitles: 'AI Error Analysis'])
      // }

      script {
        sh "python read_logs.py"
      }
    }
  }
}
