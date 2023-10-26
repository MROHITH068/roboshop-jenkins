def call()
{
    node('workstation')
            {
                stage('Code Checkout')
                        {
                            sh 'env'
                            sh 'find . | grep "^./" | xargs rm -rf'
                            if(env.TAG_NAME ==~ ".*") {
                                env.gitbrname = "refs/tags/${env.TAG_NAME}"
                            } else {
                                env.gitbrname = "${env.BRANCH_NAME}"
                            }
                            checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: "https://github.com/MROHITH068/${env.component}"]], branches: [[name: gitbrname]]], poll: false

                        }
                    if(env.cibuild == "java")
                    {
                        stage('Build'){
                                sh 'mvn package'
                        }
                    }
                if(env.cibuild == "nodejs")
                {
                    stage('Build'){
                        sh 'npm install'
                    }
                }

                    stage('Unit tests'){
                            echo 'Unit tests'
                            //sh 'npm test'
                    }

                    stage('Code Analysis'){
                            echo 'sonarqube'
//                            sh 'sonar-scanner -Dsonar.host.url=http://172.31.20.113:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.projectKey=cart'
                    }

                    stage('Security Scans'){
                            echo 'Security Scans'
                    }

                if(env.TAG_NAME==~".*") {
                    stage('Publish a Artifact')
                            {
                        sh 'rm -f Jenkinsfile'
                        sh 'echo ${TAG_NAME} > VERSION'
                        sh 'zip -r ${component}-${TAG_NAME}.zip *'
                        sh 'curl -v -u admin:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.42.230:8081/repository/${component}/${component}-${TAG_NAME}.zip'

                            }
                    }
            }
}
