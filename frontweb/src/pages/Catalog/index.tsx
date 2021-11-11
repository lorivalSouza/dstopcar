import ProductCard from 'components/ProductCard';
import Search from 'components/Search';

const Catalog = () => {
  return (
    <>    
      <div className="container my-4" id="ll">
        <Search />
        <div className="row">
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
          <div className="col-sm-6 col-lg-4">
            <ProductCard />
          </div>
        </div>
      </div>
    </>
  );
};

export default Catalog;
