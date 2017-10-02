package jvn;


public enum JvnObjectState{
	NL,  // No Lock
	RC,  // Read Lock Cached (not currently used)
	WC,  // Write Lock Cached (not currently used)
	R,   // Read Lock Taken
	W,   // Write Lock Taken
	RWC; // Write Lock Cached and Read taken
}
