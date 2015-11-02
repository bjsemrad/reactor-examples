# reactor-examples
Set of examples showing how to use the Reactor Framework.
There is two examples which shows a file word processor that will count the number of words in a set of given files.
The process of the word processor is to:
1. Stem all words
2. Remove all stop words (a, the, etc)

Once this is completed, print out the top 20 words in the files.

The two examples provided use two different models of using the reactor framework.
1. eventbus-examples
  Event bus examples use the EventBus in two different ways to demonstrate how this can be used in an application.
2. broadcaster-examples
  This contains a broadcaster example which uses a single reactor stream to process the files. It uses the default configured dispatcher.

Both examples use a poison message into the stream to know when the process has completed. This is only for the purpose of the example. In a real world application you
would want your streams/events to continue processing all the time until a shutdown signal is received which can be handled in a much more elegant way.

Please feel free to contribute other examples, or add to this if you have a better use case.

As always for detailed documentation please see the project reactor reference documentation:
http://projectreactor.io/docs/reference/
