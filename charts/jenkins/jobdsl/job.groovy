multibranchPipelineJob(jobProperties.bitbucketRepo.repository) {
  displayName(jobProperties.bitbucketRepo.repoOwner + "/" + jobProperties.bitbucketRepo.repository)
  branchSources {
    branchSource {
      source {
        bitbucket {
          id (jobProperties.bitbucketRepo.repository)
          serverUrl(jobProperties.bitbucketRepo.serverUrl)
          repoOwner(jobProperties.bitbucketRepo.repoOwner)
          repository(jobProperties.bitbucketRepo.repository)
          credentialsId(jobProperties.bitbucketRepo.credentialsId)
          traits {
            localBranchTrait()
            sshCheckoutTrait {
              credentialsId(jobProperties.gitRepo.credentialsId)
            }
            headWildcardFilter {
              includes(jobProperties.branchDiscoveryIncludes)
              excludes(jobProperties.branchDiscoveryExcludes)
            }
          }
        }
      }
      buildStrategies {
        buildNamedBranches {
          filters {
            wildcards {
              caseSensitive(true)
              includes(jobProperties.branchPushTriggerIncludes)
              excludes(jobProperties.branchPushTriggerExcludes)
            }
          }
        }
      }
    }
  }
  orphanedItemStrategy {
    discardOldItems {
      daysToKeep(14)
    }
  }
  configure {
    def traits = it / sources / data / 'jenkins.branch.BranchSource' / source / traits
    traits << 'com.cloudbees.jenkins.plugins.bitbucket.BranchDiscoveryTrait' {
      strategyId(3)
    }
  }
  configure {
    it / triggers / 'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
      spec('*/5 * * * *')
      interval('300000')
    }
  }
}
