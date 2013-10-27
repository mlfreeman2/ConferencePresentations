#!env python
#
# ZZ_Copyright_BEGIN
#
# (C) Copyright IBM Corp. 2013.
#
# ZZ_Copyright_END
#------------------------------------------------------------------------------
# NAME:  logfns.py
#
# Logging functions
#
# CHANGE ACTIVITY:
# MM/DD/YY Userid   Defect  Description
# -------- -------- ------  ---------------------------------------------------
# 08Nov10  billowen    n/a  Created
#
import logging
import sys
from logging.handlers import RotatingFileHandler

LOGFMT = '%(asctime)s %(levelname)s %(message)s'
DATFMT = '%Y-%m-%d %H:%M:%S'
LOGMAXSZ = 1024 * 1024 * 5   # 5MB
LOGSAVCNT = 3                 # save 3 old copies before rolling over


class Logger(object):

    LOG = None

    def __init__(self, fn):
        self.functionName = fn
        self.logPath = '/tmp/%s.log' % self.functionName
        if sys.platform == 'win32':
            self.logPath = 'c:\\temp\\%s.log' % self.functionName
        print 'Logging will be output to %s' % self.logPath
        self.logger = logging.getLogger(self.functionName)

        fhandler = RotatingFileHandler(self.logPath, maxBytes=LOGMAXSZ,
                                       backupCount=LOGSAVCNT)
        formatter = logging.Formatter(fmt=LOGFMT, datefmt=DATFMT)
        fhandler.setFormatter(formatter)
        self.logger.addHandler(fhandler)
        self.logger.setLevel(logging.INFO)
        Logger.LOG = self.logger

    def setVerbose(self):
        shandler = logging.StreamHandler()
        formatter = logging.Formatter(fmt=LOGFMT, datefmt=DATFMT)
        shandler.setFormatter(formatter)
        self.logger.addHandler(shandler)
        self.logger.setLevel(logging.INFO)

    def setDebug(self):
        self.logger.setLevel(logging.DEBUG)
