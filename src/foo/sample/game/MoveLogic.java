package foo.sample.game;

public class MoveLogic {

	public static void main(String[] args) {
		MoveLogic ml = new MoveLogic();

		int[] before = { 0, 2, 2, 4};
		int[] after = { 0, 0, 4, 4};
//		ml.print(before);
//		ml.print(after);
		int[] path = ml.trackMovementPath(before, after, false);
		ml.print(before);
		ml.print(after);
		ml.print(path);
	}

	public int[] trackMovementPath(int[] before, int[] after, boolean isForward) {
		if (after == null) {
			return new int[before.length];
		}

		if (!isForward) {
			int[] ps = reserve(trackForwardMovementPath(reserve(before),
					reserve(after)));
			
			for (int i = 0 ; i< ps.length ; i ++){
				if(ps[i] != 0) {
					ps[i] = before.length - ps[i] + 1;
				}
			}
			
			return ps;
		}

		return trackForwardMovementPath(before, after);
	}

	private int[] trackForwardMovementPath(int[] before, int[] after) {
		int[] path = new int[before.length];
		for (int i = 0, j = 0; i < before.length; i++) {
			if (after[j] == 0) {
				break;
			}

			if (before[i] == 0) {
				continue;
			} else if (before[i] == after[j]) {
				if(i != j){
					path[i] = j + 1;
				}
				j++;
			} else {
				path[i] = j + 1;
				after[j] = after[j] - before[i];
			}
		}

		return path;
	}

	private int[] reserve(final int[] ps) {
		int[] n = new int[ps.length];
		int i = 0;
		while (i < ps.length) {
			n[i] = ps[ps.length - 1 - i];
			i++;
		}
		return n;
	}

	public void print(int[] ps) {
		System.out.print("{ ");
		for (int p : ps) {
			System.out.print(p + " ");
		}
		System.out.println("}");
	}
}
