#!env python
#
# ZZ_Copyright_BEGIN
#
# (C) Copyright IBM Corp. 2013.
#
# ZZ_Copyright_END
#------------------------------------------------------------------------------
# NAME:  getTerHistory.py
#
# Extract history records for a specified RQM project and testplan because RQM
# does not have a user interface for showing this information.
#
# CHANGE ACTIVITY:
# MM/DD/YY Userid   Defect  Description
# -------- -------- ------  ---------------------------------------------------
# 08Nov12  billowen    n/a  Created
# 04Jun13  billowen    n/a  Polish for sharing
#

import re
import sys

import time
from optparse import OptionParser
from logfns import Logger

FUNCTION = 'GetTerHistory'
LogClass = Logger(FUNCTION)
LOG = Logger.LOG
LogClass.setVerbose()
from jazzRestMethods import getNamespaceUrl
from jazzRestMethods import getResourceFromServer
from jazzRestMethods import JTS_URLMAP
from jazzRestMethods import loginToServer
from jazzRestMethods import outputFormattedXml
from jazzRestMethods import ResourceFeed


class RQM(object):
    def __init__(self, options):
        self.rqmServerName = options.server
        self.context = options.context
        self.rqmPortNumber = options.portnum
        if self.rqmPortNumber is not None:
            self.rqmServerName = '%s:%s' % (self.rqmServerName,
                                            self.rqmPortNumber)
        self.rqmProjectArea = options.projectName
        self.testPlan = options.testPlan
        self.projectUrls = dict()
        self.resources = dict()
        self.rqmAuthCookie = None
        self.debugOn = 0
        self.rscCount = 0
        paShort = self.rqmProjectArea.split('(')[0].strip()
        self.projectName = paShort
        paShort = paShort.replace(' ', '_')
        self.outputFile = '/tmp/ter_history_%s.html' % paShort
        if sys.platform == 'win32':
            self.outputFile = 'c:\\temp\\ter_history_%s.html' % paShort

    def getFeedIterator(self, resourceType):
        return ResourceFeed(resourceType, self.rqmServerName,
                            self.rqmProjectArea, self.rqmAuthCookie,
                            nsContext=self.context)

    def getProjects(self):
        for (resourceUrl, name) in self.getFeedIterator('projects'):
            self.projectUrls[name] = resourceUrl
        try:
            self.projectAlias = self.projectUrls[self.rqmProjectArea]
        except:
            LOG.error('Could not find project %s' % self.rqmProjectArea)
            sys.exit(1)

    def findPreBuiltObject(self, resType, resName):
        # iterator over all of the objects of type resType and return
        # when we find a match for resName
        for theUrl, theTitle in self.getFeedIterator(resType):
            if theTitle == resName:
                rvUrl = theUrl
                LOG.info(resType + ' ' + resName + ':  ' + rvUrl)
                return rvUrl

        raise Exception('could not find ' + resType + ' ' + resName)

    def processResource(self, feedType, resourceDom, i, testplanUrl):
        elementsNsUrl = getNamespaceUrl('elementsNs')
        qmNsUrl = getNamespaceUrl('qmNs')
        try:
            idNd = resourceDom.find('.//{%s}identifier' % elementsNsUrl)
            idString = idNd.text.split('/').pop()
            tpNd = resourceDom.find('.//{%s}testplan' % qmNsUrl)
            if tpNd is not None:
                tpUrl = tpNd.get('href')
            else:
                tpUrl = None
                LOG.info('**** Could not find test plan url for ter %s' %
                         idNd.text)
                if self.debugOn:
                    outputFormattedXml(resourceDom)

            if testplanUrl is None or testplanUrl == tpUrl:
                i += 1
                projToken = self.projectAlias.split('/').pop()
                historyUrl = JTS_URLMAP['history'] % (self.rqmServerName,
                                                      self.context,
                                                      projToken,
                                                      feedType, idString)
                page, _ = getResourceFromServer(self.rqmAuthCookie, historyUrl,
                                                'history')
                newpage = page.replace('<title', '<p><b>Record %s <br' % i)
                newpage = newpage.replace('</title>', '</b><br>')
                newpage += '<hr>'
                return i, newpage
            else:
                return i, None
        except Exception, e:
            LOG.info('** Exception:  %s' % e)
            sys.exit(1)

    def processAllResources(self, feedType):
        self.rqmProjectArea = self.projectAlias.split('/').pop()
        LOG.info("processAllResources for project %s" % self.rqmProjectArea)
        if self.testPlan is not None:
            tpUrl = self.findPreBuiltObject('testplan', self.testPlan)
        else:
            LOG.info('No test plan specified - processing all TERs in %s' %
                     self.rqmProjectArea)
            tpUrl = None
        if tpUrl:
            historyHeader = ('<hr><p><b>TER History for Test Plan %s</b></p>' %
                             self.testPlan)
        else:
            historyHeader = ('<hr><p><b>TER History for Project %s</b></p>' %
                             self.projectName)
        allHtml = historyHeader
        self.rscCount = 0
        totalCount = 0
        for (resourceUrl, name) in self.getFeedIterator(feedType):
            totalCount += 1
            self.resources[resourceUrl] = name
            _, resourceDom = getResourceFromServer(self.rqmAuthCookie,
                                                   resourceUrl, feedType)
            if resourceDom is not None:
                self.rscCount, histHtml = self.processResource(feedType,
                                                               resourceDom,
                                                               self.rscCount,
                                                               tpUrl)
                if histHtml is not None:
                    allHtml += histHtml
                if self.debugOn:
                    outputFormattedXml(resourceDom)
            if (totalCount / 10) * 10 == totalCount:
                LOG.info('Processed %s TERs' %
                         (totalCount, self.rscCount))

        projToken = self.projectAlias.split('/').pop()
        archHistUrl = JTS_URLMAP['archhistory'] % (self.rqmServerName,
                                                   self.context,
                                                   projToken,
                                                   feedType)

        LOG.info('Collect history of deleted TERs')
        archiveHistory, _ = getResourceFromServer(self.rqmAuthCookie,
                                                  archHistUrl,
                                                  feedType)
        archiveHistory = re.sub('<title type="text"', '<br><b', archiveHistory)
        archiveHistory = re.sub('</title', ': </b', archiveHistory)

        deleteHeader = ('<hr><p><b>Deleted TERs for Project %s</b></p>' %
                        self.projectName)
        allHtml += deleteHeader + archiveHistory
        f = open(self.outputFile, 'w')
        f.write(allHtml)
        f.close()
        LOG.info('TER history published to file %s' % self.outputFile)


def processInputArgs():
    versionString = '%prog ' + '1.0.0'
    parser = OptionParser(version=versionString)
    parser.add_option('-u', '--username',
                      help='Username for authentication.')
    parser.add_option('-p', '--password',
                      help='Password for authentication.')
    parser.add_option('-s', '--server',
                      help='RQM server name to access.')
    parser.add_option('--portnum',
                      help='Port number on server to connect with.')
    parser.add_option('-c', '--context',
                      help='Jazz context, default value is "qm"',
                      default='qm')
    parser.add_option('-n', '--projectName',
                      help='Full project name.')
    parser.add_option('-t', '--testPlan',
                      help='''Test Plan to check TERs from''')

    parser.add_option('-v', '--verbose', default=False,
                      help='Generate verbose output', action='store_true')

    (options, _) = parser.parse_args()

    if options.verbose:
        LOG.info('Verbose output')
        LogClass.setDebug()

    errCnt = 0
    for attr in ['password', 'server', 'projectName']:
        if getattr(options, attr) is None:
            LOG.error('Missing required parameter "%s"' % attr)
            errCnt += 1
    if errCnt > 0:
        LOG.error('Exiting because of %s input errors' % errCnt)
        sys.exit(1)

    return options


def main(argv):
    options = processInputArgs()
    try:
        rqm = RQM(options)
        rqm.rqmAuthCookie = loginToServer(rqm.rqmServerName, options.username,
                                          options.password,
                                          rqm.context)
        rqm.getProjects()

        try:
            rqm.processAllResources('executionworkitem')

        except KeyError:
            LOG.info('ERROR:  Unknown project area %s' % options.projectName)

        LOG.info('%s:  TOTAL of %s resources processed.' % (time.asctime(),
                                                            rqm.rscCount))
    except Exception, e:
        LOG.info('General Exception : %s' % e)

if __name__ == "__main__":
        main(sys.argv)
