def call()
{
    node('workstation')
            {
                    if(env.cibuild == "java")
                    {
                        stage('Build'){
                                sh 'mvn package'
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
                    stage('Publish a Artifact'){
                        echo 'Publish a Artifact'
                        sh 'env'
                        }
                    }
            }
}
