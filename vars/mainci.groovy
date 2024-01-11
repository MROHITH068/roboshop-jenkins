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

                    stage('Docker Build'){
                            sh 'docker build -t ${component} .'
                    }

//                if(env.TAG_NAME==~".*") {
//                    stage('Publish a Artifact')
//                            {
//                                if(env.cibuild == "java")
//                                {
//                                sh 'mv target/${component}-1.0.jar ${component}.jar'
//                                sh 'rm -f pom.xml src target'
//                                }
//                        sh 'rm -rf Jenkinsfile'
//                        sh 'echo ${TAG_NAME} > VERSION'
//                        sh 'zip -r ${component}-${TAG_NAME}.zip *'
//                        sh 'curl -v -u admin:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.42.230:8081/repository/${component}/${component}-${TAG_NAME}.zip'
//
//                            }
//                    }

                if(env.TAG_NAME==~".*") {
                    stage('Publish a Artifact')
                            {
                                sh 'docker tag ${component} 403215663985.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME}'
                                sh 'aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 403215663985.dkr.ecr.us-east-1.amazonaws.com'
                                sh 'docker push 403215663985.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME}'
                            }
                }

            }
}
