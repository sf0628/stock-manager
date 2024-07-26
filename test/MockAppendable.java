import java.io.IOException;

/**
 * Represents a mock Appendable interface for testing.
 * Configured to throw an IOException when append methods are called.
 */
public class MockAppendable implements Appendable {
  private boolean shouldThrow;

  /**
   * Constructs a MockAppendable object.
   *
   * @param shouldThrow boolean that if true, the IOException should be throw,
   *                    else append behaves normally
   */
  public MockAppendable(boolean shouldThrow) {
    this.shouldThrow = shouldThrow;
  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    if (shouldThrow) {
      throw new IOException("Mock IO Exception");
    }
    return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    if (shouldThrow) {
      throw new IOException("Mock IO Exception");
    }
    return this;
  }

  @Override
  public Appendable append(char c) throws IOException {
    if (shouldThrow) {
      throw new IOException("Mock IO Exception");
    }
    return this;
  }
}
