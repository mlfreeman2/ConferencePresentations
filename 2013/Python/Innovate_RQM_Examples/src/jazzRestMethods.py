#!env python
#
# ZZ_Copyright_BEGIN
#
# (C) Copyright IBM Corp. 2013.
#
# ZZ_Copyright_END
#------------------------------------------------------------------------------
# NAME:  jazzRestMethods.py
#
# Jazz communication methods
#
# CHANGE ACTIVITY:
# MM/DD/YY Userid   Defect  Description
# -------- -------- ------  ---------------------------------------------------
# 08Nov10  billowen    n/a  Created
#
import sys
import urllib
import urllib2
import urlparse
import cookielib
import xml.etree.ElementTree as ET
from logfns import Logger

try:
    import oauth2 as oauth
    OAUTH_IMPORT = True
except:
    OAUTH_IMPORT = False

LOG = Logger.LOG

jbase = ('https://%s/%s/service/com.ibm.rqm.integration.service.'
         'IIntegrationService')
JTS_URLMAP = {'identity':       'https://%s/%s/authenticated/identity',
              'security_check': 'https://%s/%s/j_security_check',
              'jazz_security_check': 'https://jazz.net/auth/login',
              'resource':       jbase + '/resources/%s/%s/',
              'archhistory':    jbase + '/history?resourceId=resources/%s/%s/',
              'history':        jbase + '/history?resourceId=resources/%s/%s/%s',
              'projects':       jbase + '/projects',
              'feed':           jbase + '/resources/%s/%s/?abbreviate=false'
              }

OAUTH_URLMAP = {'request_token_url':      'https://%s/jts/oauth-request-token',
                'authorize_url':          'https://%s/jts/oauth-authorize',
                'access_token_url':       'https://%s/jts/oauth-access-token',
                'protected_resource_url': 'https://%s/rm/discovery/RMCatalog',
                }

qbase = 'http://jazz.net/xmlns/alm/qm'
NAMESPACE_MAP = {'atomNs':       'http://www.w3.org/2005/Atom',
                 'taskNs':       qbase + '/qmadapter/task/v0.1',
                 'adapterNs':    qbase + '/qmadapter/v0.1',
                 'qmNs':         qbase + '/v0.1/',
                 'catalogNs':    qbase + '/v0.1/catalog/v0.1',
                 'erNs':         qbase + '/v0.1/executionresult/v0.1',
                 'ewiNs':        qbase + '/v0.1/executionworkitem/v0.1',
                 'testScriptNs': qbase + '/v0.1/testscript/v0.1/',
                 'testSuiteNs':  qbase + '/v0.1/ts/v0.1/',
                 'testSuitelNs': qbase + '/v0.1/tsl/v0.1/',
                 'almNs':        'http://jazz.net/xmlns/alm/v0.1/',
                 'elementsNs':   'http://purl.org/dc/elements/1.1/',
                 'vegaNs':       'http://schema.ibm.com/vega/2008/',
                 'jfsNs':        'http://jazz.net/xmlns/prod/jazz/jfs/1.0/'
                 }


# global functions
def getNamespaceUrl(nsKey):
    # create a dictionary of namespaces
    try:
        nsUrl = NAMESPACE_MAP[nsKey]
    except KeyError:
        nsUrl = ''
    return nsUrl


def outputHeaders(hmap, htype='Request'):
    fstr = '\n%s headers:\n' % htype
    for k in hmap:
        fstr += ' %s: %s\n' % (k, hmap[k])
    fstr += '\n'
    return fstr


def getCookieString(response, request):
    cj = cookielib.CookieJar()
    cookieStr = ''
    for c in cj.make_cookies(response, request):
        cookieStr += ' %s=%s;' % (c.name, c.value)
    return cookieStr


# get a resource from the server
def getResourceFromServer(authCookie, resourceUrl, feedtype=''):
    tcDom = None
    LOG.debug('get url %s' % resourceUrl)
    get_request = urllib2.Request(resourceUrl)
    get_request.add_header('accept', 'application/xml')
    get_request.add_header('Cookie', authCookie)
    try:
        page = urllib2.urlopen(get_request)
        page = page.read()
        tcDom = ET.fromstring(page)
    except IOError, e:
        LOG.error('IOERROR Could not get %s %s code: %s' %
                  (feedtype, resourceUrl, str(e)))
    except Exception, e:
        LOG.error('EXCEPTION Could not get %s %s code: %s' %
                  (feedtype, resourceUrl, str(e)))
    return page, tcDom


def getOslcResourceFromServer(authCookie, resourceUrl):
    tcDom = None
    LOG.debug('GET url:     %s' % resourceUrl)
    get_request = urllib2.Request(resourceUrl)
    get_request.add_header('Cookie', authCookie)
    get_request.add_header('accept', 'application/rdf+xml')
    get_request.add_header('OSLC-Core-Version', '2.0')
    try:
        page = urllib2.urlopen(get_request)
        page = page.read()
        tcDom = ET.fromstring(page)
    except IOError, e:
        LOG.error('Could not get resource  code:  %s' % str(e.code))

    return page, tcDom


def getRmResourceFromServer(client, resourceUrl):
    tcDom = None
    LOG.debug('GET rm url: %s' % resourceUrl)
    try:
        response = client.request(resourceUrl)
        page = response[1]
        tcDom = ET.fromstring(page)
    except Exception, e:
        LOG.error('Could not get resource  code:  %s' % str(e))

    return page, tcDom


# insert a resource
def putResourceToServer(tcDom, authCookie, resourceUrl, resourceType=''):
    put_request = urllib2.Request(resourceUrl, data=ET.tostring(tcDom))
    put_request.add_header('Cookie', authCookie)
    put_request.get_method = lambda: 'PUT'
    LOG.info('Put %s to %s' % (resourceType, resourceUrl))
    try:
        page = urllib2.urlopen(put_request)
        LOG.debug('Update %s %s - status: %s' % (resourceType,
                                                 resourceUrl,
                                                 str(page.code)))
    except IOError, e:
        if e.code == 201:
            LOG.debug('Insert %s %s - status: %s' % (resourceType,
                                                     resourceUrl,
                                                     str(e.code)))
        else:
            err_msg = 'Could not insert %s %s' % (resourceType, resourceUrl)
            LOG.error(err_msg)
            LOG.error('Return code = ' + str(e.code))
            raise Exception(err_msg)


def putOslcResourceToServer(theDom, authCookie, resourceUrl):
        put_request = urllib2.Request(resourceUrl, data=ET.tostring(theDom))
        put_request.add_header('Cookie', authCookie)
        put_request.add_header('accept', 'application/rdf+xml')
        put_request.add_header('OSLC-Core-Version', '2.0')
        put_request.add_header('Content-type', 'application/xml')

        put_request.get_method = lambda: 'PUT'
        try:
            page = urllib2.urlopen(put_request)
            LOG.info('Update ' + resourceUrl + ' status: ' + str(page.code))
        except IOError, e:
            if e.code in [201]:
                LOG.info('Insert ' + resourceUrl + ' status: ' + str(e.code))
            else:
                LOG.warning('Could not update or insert ' + resourceUrl)
                LOG.warning('Return code = ' + str(e.code))
                raise Exception('Could not update or insert ' + resourceUrl)


def postResourceToServer(theDom, authCookie, resourceUrl):
    post_request = urllib2.Request(resourceUrl, data=ET.tostring(theDom))
    post_request.add_header('Cookie', authCookie)
    post_request.add_header('accept', 'text/xml')
    post_request.add_header('Content-Type', 'text/xml')
    try:
        page = urllib2.urlopen(post_request)
        LOG.info('Update %s, status: %s' % (resourceUrl, str(page.code)))
    except IOError, e:
        if e.code in [201]:
            LOG.debug(outputHeaders(page.info(), type='Response'))
            LOG.info('Insert %s, status: ' % (resourceUrl, str(e.code)))
        else:
            LOG.error('Could not insert %s' % resourceUrl)
            LOG.error('Return code:  %s' % str(e.code))
            raise Exception('Could not insert ' + resourceUrl)
    return page.info()['Location']


def loginToServer(serverName, userName, password, nsContext='qm'):
    LOG.info('Log into %s Server %s as %s ' %
             (nsContext, serverName, userName))
    authData = {'j_username': userName,
                'j_password': password}
    # original page request - allows us to get session cookie information
    get_request = urllib2.Request(JTS_URLMAP['identity'] %
                                  (serverName, nsContext))
    page = urllib2.urlopen(get_request)
    authCookie = getCookieString(page, get_request)
    if serverName == 'jazz.net':
        post_request = urllib2.Request(JTS_URLMAP['jazz_security_check'])
    else:
        post_request = urllib2.Request(JTS_URLMAP['security_check'] %
                                       (serverName, nsContext))
    post_request.add_header('Cookie', authCookie)
    post_request.add_data(urllib.urlencode(authData))
    page = urllib2.urlopen(post_request)
    authCookie = getCookieString(page, post_request)
    # should be redirected - if not, we likely have bad user id or password
    if page.geturl() == JTS_URLMAP['security_check'] % (serverName, nsContext):
        raise Exception('Could not authenticate user - check user id and pw')
    LOG.info('Successfully logged into %s' % (serverName))
    return authCookie


def loginToRmServer(serverName, userName, password, friendPath=None):

    if friendPath is None:
        friendPath = '/opt/IBM/JazzTeamServer/server/conf/rm/friendsconfig.rdf'

    LOG.info('Log into RM Server %s/rm as %s' % (serverName, userName))
    if not OAUTH_IMPORT:
        LOG.error('Could not import oauth package.')
        LOG.error('This is required to interface with RM.  Exiting...')
        sys.exit(1)

    friendsfile = open(friendPath, 'r')
    dom = ET.fromstring(friendsfile.read())
    consumer_key = dom.find('.//{%s}oauthConsumerKey' %
                            NAMESPACE_MAP['jfsNs']).text
    consumer_secret = dom.find('.//{%s}oauthConsumerSecret' %
                               NAMESPACE_MAP['jfsNs']).text

    consumer = oauth.Consumer(consumer_key, consumer_secret)
    client = oauth.Client(consumer)

    # step 1:  get token
    resp, content = client.request(OAUTH_URLMAP['request_token_url'] %
                                   serverName, "POST")
    if resp['status'] != '200':
        raise Exception("Invalid response %s." % resp['status'])

    request_token = dict(urlparse.parse_qsl(content))
    auth_url_with_token = (' %s?oauth_token=%s' %
                           (OAUTH_URLMAP['authorize_url'] % serverName,
                            request_token['oauth_token']))

    # step 2:  authenticate to jts server
    cookie = loginToServer(serverName, userName, password, 'jts')

    # step 3:  get authorize url using token from step 1
    getResourceFromServer(cookie, auth_url_with_token)

    # create a new client object that contains our request token and secret
    token = oauth.Token(request_token['oauth_token'],
                        request_token['oauth_token_secret'])
    client = oauth.Client(consumer, token)

    # step 4:  post the access token url
    resp, content = client.request(OAUTH_URLMAP['access_token_url'] %
                                   serverName, "POST")
    access_token = dict(urlparse.parse_qsl(content))

    # now create a new client using access_token & access_token_secret
    token = oauth.Token(access_token['oauth_token'],
                        access_token['oauth_token_secret'])
    client = oauth.Client(consumer, token)
    return client


class ResourceFeed:
    ''' Create a python iterator for any RQM resource class
    @param resourceType: String resource type to iterator over; i.e. testcase,
                         executionresult, etc.
    @param serverName:   String consisting of RQM server name (and port number
                         if required)
    @param projectArea:  String RQM project area
    @param authCookie:   String authentication cookie obtained in initial
                         login to RQM server
    @param feedFilter:   Optional string containing a filter suffix for the
                         feed url
    @return:             The iterator returns a string URL for the next entry
                         in the resource feed'''
    def __init__(self, resourceType, serverName, projectArea, authCookie,
                 nsContext='qm', feedFilter=None):
        self.atomNsUrl = NAMESPACE_MAP['atomNs']
        self.resourceType = resourceType
        self.authCookie = authCookie
        self.curEntry = 0
        self.nextPage = None
        if self.resourceType == 'projects':
            self.feedUrl = JTS_URLMAP[self.resourceType] % (serverName,
                                                            nsContext)
        else:
            self.feedUrl = JTS_URLMAP['resource'] % (serverName,
                                                     nsContext,
                                                     projectArea,
                                                     self.resourceType)
        if feedFilter is not None:
            self.feedUrl += feedFilter

        _, tcDom = getResourceFromServer(authCookie, self.feedUrl,
                                         self.resourceType)
        for link in tcDom.findall('./{%s}link' % self.atomNsUrl):
            if link.get('rel') == 'next':
                self.nextPage = link.get('href')
        self.entries = tcDom.findall('.//{%s}entry' % self.atomNsUrl)

    def next(self):
        try:
            rv = (self.entries[self.curEntry].find('{%s}id' %
                                                   self.atomNsUrl).text,
                  self.entries[self.curEntry].find('{%s}title' %
                                                   self.atomNsUrl).text)
            self.curEntry += 1
        except IndexError:
            if self.nextPage is not None:
                _, tcDom = getResourceFromServer(self.authCookie,
                                                 self.nextPage,
                                                 self.resourceType)
                self.nextPage = None
                self.curEntry = 0
                for link in tcDom.findall('./{%s}link' % self.atomNsUrl):
                    if link.get('rel') == 'next':
                        self.nextPage = link.get('href')
                self.entries = tcDom.findall('.//{%s}entry' % self.atomNsUrl)
                rv = (self.entries[self.curEntry].find('{%s}id' %
                                                       self.atomNsUrl).text,
                      self.entries[self.curEntry].find('{%s}title' %
                                                       self.atomNsUrl).text)
                self.curEntry += 1
            else:
                raise StopIteration
        return rv

    def __iter__(self):
        return self


# utility to indent an XML element tree
def indentElementTree(elem, level=0):
        i = "\n" + level*"  "
        if len(elem):
            if not elem.text or not elem.text.strip():
                elem.text = i + "  "
            for child in elem:
                indentElementTree(child, level+1)
            if not child.tail or not child.tail.strip():
                child.tail = i
            if not elem.tail or not elem.tail.strip():
                elem.tail = i
        else:
            if level and (not elem.tail or not elem.tail.strip()):
                elem.tail = i


def outputFormattedXml(tcDom, outputObj=sys.stdout):
    indentElementTree(tcDom)
    tree = ET.ElementTree(tcDom)
    tree.write(outputObj)
