# Welcome to MRPack
Hadoop based Concurrent algorithms in a single MapReduce Job:
MRPack is a variant of MapReduce implementation where multiple related algorithms can be executed 
concurrently in a single MapReduce job. In MRPack, All algorithms are implemented in Map and 
Reduce programming model and all these algorithms are then executed under single unified Map and
Reduce super class. MRPack uses Main Map and reduce, and Sub map and reduce approach to implementation.
It introduces a composite key structure for intermediate data handling.
## Main Map and Reduce##
MRPack follows a hierarchical structure of executing mappers and reducers of all algorithms in a 
single MapReduce job where individual map function of each job are executed inside the main Map of
the Job. similarly, reducer functions of all the algorithms are executed inside the Reduce of main Job.
## Keys to Intermediate data##
The data generated by map steps is differentiated based on an organized key structure called composite 
key structure. This is an abstruct key class which contains the references of all the keys of involved 
algorithms in the job. Custom partitioner and comparator functions operate on these keys to distinguish
between intermediate data of each algorithm and forward them to an appropriate Reducer. By appropriate 
reducer, we mean that the data of a single algorithm can be forwarded to a separate reducer to generate 
separate output. 
The method to operate the MRPack is detailed as follows:
It cintains implementation for KNN, KMeans, C4.5, InvertedIndex, and WordCount algorithms.
## Getting started:##
**MRPack on Eclipse**
>Install Hadoop and
 1. Install eclipse and configure with Hadoop, if you want to use eclipse 
 2. Open MRPack in Mapreduce Perspective
 3. Set the arguments in Eclipse : Input directiry for data and output directory at Hadoop
 4. Execute as an Hadoop Job. (Run on Hadoop option)

**MRPack asd an executable Jar**
>Install Hadoop and then
 1. Creat Jar file from MRPack in Eclipse by "Export as jar" option. Remember to set the main class of the jar.
 2. Execute the Jar file on Ubuntu terminal followed by input data directory and output data directory in Hadoop.
  * For example: 
  * hdfs jar MRPack.jar /home/****/hduser/input/data   /home/****/hduser/output/data
 
*We have implemented for the data to be simple text format. If your data format changes, then you better change 
only implementation of the Main mapper function of the main job.*

