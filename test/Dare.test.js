const Dare = artifacts.require("Dare");

contract("Dare test", async (accounts) => {
  let contract;

  before(async () => {
    contract = await Dare.deployed();
  });

  describe("deployment", async () => {
    it("deploys successfully", async () => {
      const address = contract.address;
      console.log(address);
      assert.notEqual(address, 0x0);
      assert.notEqual(address, "");
      assert.notEqual(address, null);
      assert.notEqual(address, undefined);
    });
  });
});
