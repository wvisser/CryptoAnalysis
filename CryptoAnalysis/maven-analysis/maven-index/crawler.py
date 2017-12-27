import urllib
from HTMLParser import HTMLParser
import xml.etree.ElementTree as ET

ROOTURL = "http://central.maven.org/maven2/"
OUTPUT_FILE = "maven-central-index.txt"

requestCount = 0
# create a subclass and override the handler methods
class RecursiveMavenCrawler(HTMLParser):

	def __init__(self,url):
		HTMLParser.__init__(self)
		self.url = url
		self.containsJar = False
	def handle_data(self,data):
		if(data.endswith("/") and not data.endswith("../")):
			subParser = RecursiveMavenCrawler(self.url+data)
			subParser.run()
		if(data.endswith(".jar")):
			self.containsJar = True
		if(data.endswith("maven-metadata.xml") and not self.containsJar):
			root = ET.fromstring(urllib.urlopen(self.url+data).read())
			if root.find("groupId") is None:
				return
			groupId = root.find("groupId").text
			if root.find("artifactId") is None:
				return
			artifactId = root.find("artifactId").text
			latest = root.find("*/latest")
			if latest is None:
				return
			version = latest.text
			# Write the file out again
			with open(OUTPUT_FILE, 'a') as file:
					file.write("'"+groupId +":"+artifactId+":_:"+version+"'\n")
			print(groupId +":"+artifactId+":_:"+version)
				
	def run(self):
		global requestCount
		requestCount += 1
		if(requestCount % 100 == 0):
			print("Made " + str(requestCount) + " requests!")
		self.feed(urllib.urlopen(self.url).read())

# instantiate the parser and fed it some HTML
parser = RecursiveMavenCrawler(ROOTURL)
parser.run()
 