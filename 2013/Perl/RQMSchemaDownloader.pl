#!/usr/bin/perl
use LWP::Simple;
use LWP::UserAgent;
use Data::Dumper;
use File::Basename;
use Tie::File;
use XML::LibXML;

my $url = '';
my $userName = '';
my $pword = '';
my $debug = 0;

my $schema = 'service/com.ibm.rqm.integration.service.IIntegrationService/schema/';

my $dirname = dirname(__FILE__);

my $ua = LWP::UserAgent->new();
$ua->cookie_jar( {} );

$url .= "/" if ($url !~ /\/^/);

print "Logging in to RQM at $url.\n";
my $loginResponse = $ua->post( $url . 'j_security_check', { 'j_username' => $userName, 'j_password' => $pword } );
print $loginResponse->as_string if ($debug);

my $files;
$files->{'qm.xsd'} = $url . $schema . "qm.xsd";
@xsds = ('qm.xsd');

$files->{'feed.xsd'} = $url . $schema . "feed.xsd";
push @xsds, 'feed.xsd';

print "Fetching $file.\n";
$response = $ua->get($files->{$file}, ':content_file' => $dirname . "//" . $file);
print $response->as_string if ($debug);

foreach $file (@xsds) 
{
	if (-e $dirname . "//" . $file) {
		print "$file already exists.\n";
		next;
	}
	print "Fetching $file.\n";
	$response = $ua->get($files->{$file}, ':content_file' => $dirname . "//" . $file);
	print $response->as_string if ($debug);
	
	# Create an instance of our XML parser
	my $parser = XML::LibXML->new;

	eval { $doc = $parser->parse_file( $dirname . "//" . $file) };

	if ( !defined $doc )
	{
		die "Unable to parse $filefrom RQM server at $url.\n";
	}

	my @nodelist = $doc->getElementsByTagNameNS('http://www.w3.org/2001/XMLSchema', 'import');

	foreach my $node (@nodelist) 
	{
		my $schemaURL = $node->getAttribute('schemaLocation');
		if ($schemaURL =~ /\/(\w+\.xsd)$/) {
			if (!defined $files->{$1}) {
				$files->{$1} = $schemaURL;
				push @xsds, $1;
			}
			$node->setAttribute('schemaLocation', $1);
		}
	}

	$doc->toFile($dirname . "//" . $file, 0);
}

print "\n=======\n";
print "Recommended xsd.exe settings:\n";
print "xsd.exe /l:CS /f /c ";
for my $i (reverse 0..$#xsds) {
   print "$xsds[$i] ";
}
print "\n";