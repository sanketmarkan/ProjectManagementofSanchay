from py4j.java_gateway import JavaGateway

gateway = JavaGateway()

story = gateway.createSSFStory()

story.readFile("workspace/syn-annotation/Premchand/poslcat-single-fs-1.txt", "UTF-8")

count = story.countSentences()

print("Number of sentences: " + str(count))

sen = story.getSentence(0)

print(sen.makeString())

root = sen.getRoot()

leaves = root.getAllLeaves()

print("Number of words: " + str(len(leaves)))

w1 = leaves[0]

print(w1.makeString())

print("Changing tag:")

w1.setName("NewTag")

print(w1.makeString())

fs = w1.getFeatureStructures().getAltFSValue(0)

print(fs.makeString())

print("Changing attribute value:")

fs.setAttributeValue("poslcat", "nnp")

print(fs.makeString())

print("Querying the story:")

query = gateway.createQuery("C.t~'JJ'")

qmap = story.getMatchingValues(query)

print str(len(qmap)) + " Matches found"

for k,v in qmap.items():
	print(k.toString())
	print(v)


