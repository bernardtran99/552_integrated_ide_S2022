public enum Operation {
  ADD("+"){
    @Override
    int apply(int a, int b) {
      return a + b;
    }
  },
  SUBTRACT("-"){
    @Override
    int apply(int a, int b) {
      return a-b;
    }
  },
  MULTIPLY("*"){
    @Override
    int apply(int a, int b) {
      return a * b;
    }
  },
  DIVIDE("/"){
    @Override
    int apply(int a, int b) {
      return a * b;
    }
  };

  private final String symbol;

  Operation(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return symbol;
  }

  int apply(int a, int b){
    return 0;
  };

  public static Operation fromSymbol(char symbol){
    switch (symbol){
      case '+':
        return ADD;
      case '-':
        return SUBTRACT;
      case '*':
        return MULTIPLY;
      case '/':
        return DIVIDE;
      default:
        return null;
    }
  }
}
